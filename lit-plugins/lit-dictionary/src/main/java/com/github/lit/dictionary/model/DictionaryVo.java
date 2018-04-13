package com.github.lit.dictionary.model;

import lombok.Data;

/**
 * User : liulu
 * Date : 2018/3/26 16:30
 * version $Id: DictionaryVo.java, v 0.1 Exp $
 */
@Data
public abstract class DictionaryVo {

    private String dictKey;

    private String dictValue;

    private Integer orderNum;

    private String remark;

    @Data
    public static class Add extends DictionaryVo {
        private Long parentId;
    }

    @Data
    public static class Update extends DictionaryVo {
        private Long id;
    }

    @Data
    public static class Detail extends DictionaryVo {
        private Long id;
    }

}
