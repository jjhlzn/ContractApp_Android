package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.Product;

/**
 * Created by lzn on 16/4/8.
 */
public class GetProductResponse extends ServerResponse {
    private Product mProduct;

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }
}
