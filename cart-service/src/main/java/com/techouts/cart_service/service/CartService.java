package com.techouts.cart_service.service;

import com.techouts.cart_service.model.Cart;
import com.techouts.cart_service.model.CartItem;
import com.techouts.cart_service.repository.CartItemRepo;
import com.techouts.cart_service.repository.CartRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private CartRepo cartRepoImpl;
    private CartItemRepo cartItemRepoImpl;
//    private ProductRepo productRepoImpl;
//    private UserRepo userRepoImpl;

    CartService(CartRepo cartRepoImpl, CartItemRepo cartItemRepoImpl) {
        this.cartRepoImpl = cartRepoImpl;
        this.cartItemRepoImpl = cartItemRepoImpl;
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCartItemsByUser(int userId) {

        Cart userCart = cartRepoImpl.findByUserId(userId);
        return userCart.getCartItemList ();

    }

//    public float[] calculateTotalCartPrice(List<CartItem> userCartItems) {
//
//        float result = 0;
//
//        for (CartItem cartItem : userCartItems) {
//
//            int productQuantity = cartItem.getQuantity();
//            float productPrice = cartItem.getProductId().getPrice();
//
//            result = result + (productPrice * productQuantity);
//
//        }
//
//        return new float[] {(float) (result + (result * 0.18)), (float) (result * 0.18)};
//
//    }

    @Transactional
    public boolean addToCart(int user, int productId, int quantity) {

        // TODO

        return false;
    }

//    @Transactional
//    public CartItem removeFromCart(int cartItemId) {
//
//        CartItem cartItemInQuestion = cartRepoImpl.getCartItem(cartItemId);
//
//        if (cartItemInQuestion == null) {
//            return null;
//        }
//
//        cartRepoImpl.removeItemFromCart(cartItemInQuestion);
//
//        return cartItemInQuestion;
//
//    }
//
//    @Transactional
//    public void changeProductQuantity(int cartItemId, boolean isAddition) {
//
//        CartItem cartItem = cartRepoImpl.getCartItem(cartItemId);
//
//        if (isAddition) {
//            cartItem.setQuantity(cartItem.getQuantity() + 1);
//        } else {
//            cartItem.setQuantity(cartItem.getQuantity() - 1);
//
//            if (cartItem.getQuantity() <= 0) {
//                removeFromCart(cartItemId);
//                return;
//            }
//        }
//
//        cartRepoImpl.updateItemInCart(cartItem);
//
//    }

}
