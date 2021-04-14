package com.example.search.search.service;

import com.clx4399.common.to.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-04-14 11:13:02
 */
public interface ProductSaveService {

    /**
     * @param skuEsModels
     * @return boolean
     * @author CLX
     * @describe:
     * @date 2021/4/14 11:37
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
