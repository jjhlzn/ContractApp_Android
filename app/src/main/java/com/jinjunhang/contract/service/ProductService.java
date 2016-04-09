package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.Product;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzn on 16/4/8.
 */
public class ProductService extends BasicService {

    public GetProductResponse  GetProductById(String id) {
        String url = makeGetProductByIdUrl(id);
        return sendRequest(url, GetProductResponse.class, new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                GetProductResponse resp = (GetProductResponse)response;
                boolean isExist = json.getBoolean("isExist");
                if (isExist) {
                    JSONObject productJson = json.getJSONObject("product");
                    Product product = new Product();
                    product.setChineseName(productJson.getString("chineseName"));
                    product.setEnglishName(productJson.getString("englishName"));
                    product.setHuohao(productJson.getString("huohao"));
                    product.setXinghao(productJson.getString("xinghao"));
                    product.setChineseDesc(productJson.getString("chineseDesc"));
                    product.setEnglishDesc(productJson.getString("englishDesc"));
                    product.setChengbenPrice(productJson.getDouble("chengbenPrice"));
                    product.setSellPrice(productJson.getDouble("sellPrice"));
                    boolean isImageExist = productJson.getBoolean("hasImage");
                    if (isImageExist) {
                        //productJson.get
                    }
                    resp.setProduct(product);
                }
                return resp;
            }
        });
    }

    private String makeGetProductByIdUrl(String id) {
        return String.format(ServiceConfiguration.GetProductUrl + "?id=%s", id);
    }

}
