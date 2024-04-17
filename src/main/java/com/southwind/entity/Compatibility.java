package com.southwind.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2024-04-12
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Compatibility implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @TableField("product_Id1")
    private Integer productId1;

    @TableField("product_Id2")
    private Integer productId2;
  /**
   * 适配(1:适配 0：不适配；默认值)
   */
    private Integer compatible;

    private String reason;


}
