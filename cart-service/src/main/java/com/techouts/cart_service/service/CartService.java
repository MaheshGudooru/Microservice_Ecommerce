package com.techouts.cart_service.service;

import com.techouts.cart_service.dto.CartItemDTO;
import com.techouts.cart_service.model.Cart;
import com.techouts.cart_service.model.CartItem;
import com.techouts.cart_service.repository.CartItemRepo;
import com.techouts.cart_service.repository.CartRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class CartService {

    private final CartRepo cartRepoImpl;
    private final CartItemRepo cartItemRepoImpl;

    CartService(CartRepo cartRepoImpl, CartItemRepo cartItemRepoImpl) {
        this.cartRepoImpl = cartRepoImpl;
        this.cartItemRepoImpl = cartItemRepoImpl;
    }

    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartItemsByUser(int userId) {

        Cart userCart = cartRepoImpl.findByUserId(userId).orElse(null);

        if (userCart != null) {

            List<CartItem> currUserCartItem = userCart.getCartItemList();
            List<CartItemDTO> cartItemDTOList = new ArrayList<>();
            for(CartItem cartItem : currUserCartItem) {

                CartItemDTO cartItemDTO = new CartItemDTO(cartItem.getId(), cartItem.getProductId(), cartItem.getQuantity());
                cartItemDTOList.add(cartItemDTO);

            }
            return cartItemDTOList;

        }

        return new ArrayList<>();

    }

    @Transactional
    public Cart createUserCart(int userId) {

        Cart userCart = cartRepoImpl.findByUserId(userId).orElse(null);

        if(userCart != null) {
            return userCart;
        }

        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setCartItemList(new ArrayList<>());
        cartRepoImpl.save(newCart);

        return newCart;
    }

    @Transactional
    public boolean addToCart(int userId, int productId, int quantity) {

        Cart userCart = cartRepoImpl.findByUserId(userId).orElse(null);

        if(quantity <= 0) {
            return false;
        }

        if (userCart == null) {

            Cart newlyCreatedCart = createUserCart(userId);
            newlyCreatedCart.getCartItemList().add(new CartItem(newlyCreatedCart, productId, quantity));

            return true;
        }

        CartItem currCartItem = cartItemRepoImpl.findByProductId(productId).orElse(null);

        if(currCartItem == null) {

            userCart.getCartItemList().add(new CartItem(userCart, productId, quantity));
            return true;

        }

        currCartItem.setQuantity(quantity);
        return true;

    }

    @Transactional
    public String removeCartItemFromCart(int cartItemId, int userId) {
        CartItem cartItem = cartItemRepoImpl.findById(cartItemId).orElse(null);

        if(cartItem == null) return "cart item not found";

        if(cartItem.getCartId ().getUserId () != userId) {
            return "unauthorized";
        }

        cartRepoImpl.findById(cartItem.getCartId().getId()).orElse(new Cart()).getCartItemList().remove(cartItem);
        cartItemRepoImpl.delete(cartItem);

        return "success";

    }

    @Transactional
    public String RemoveAllCartItemsFromCart(int userId) {

        Cart userCart = cartRepoImpl.findByUserId (userId).orElse (null);

        if(userCart == null) {
            return "Cart does not exist for this user";
        }

        userCart.getCartItemList ().clear ();
        cartRepoImpl.save (userCart);

        return "Successfully removed all cart items from the cart";

    }

    @Transactional
    public String updateCartItemQuantity(int userId, int cartItemId, int quantity) {
        CartItem cartItem = cartItemRepoImpl.findById(cartItemId).orElse(null);

        if (cartItem == null) return "cart item not found";

        if(cartItem.getCartId ().getUserId () != userId) {
            return "unauthorized";
        }

        if(quantity <= 0) {
            removeCartItemFromCart (cartItemId, userId);
            return "Cart item removed since quantity is less than zero";
        }

        cartItem.setQuantity (quantity);

        cartItemRepoImpl.save(cartItem);

        return "cart item quantity successfully updated";
    }
}
