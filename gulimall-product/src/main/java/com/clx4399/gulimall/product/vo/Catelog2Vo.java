package com.clx4399.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 二级分类vo
 * @date 2021-04-15 20:59:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {

    private String catalog1Id;
    private List<Object> catalog3List;
    private String id;
    private String name;

    /**
     * 三级分类vo
     * */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Catalog3Vo{

        private String catalog2Id;
        private String id;
        private String name;

    }

}
