package com.southwind.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @since 2024-04-19
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Scheme implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键
     */
        @TableId(value = "scheme_id", type = IdType.AUTO)
      private Integer schemeId;

    private String schemeName;

    private Integer userId;

      /**
     * 用户名
     */
      private String loginName;

    private Integer motherboard;

    private Integer cpu;

    private Integer memory;

    private Integer powerSupply;

    private Integer graphicsCard;

    private Integer hardDrive;

    private Integer computerBox;

      /**
     * 创建时间
     */
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      /**
     * 更新时间
     */
        @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;


}
