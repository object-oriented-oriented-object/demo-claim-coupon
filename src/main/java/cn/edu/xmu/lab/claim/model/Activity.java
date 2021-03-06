package cn.edu.xmu.lab.claim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column activity.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column activity.quantity
     *
     * @mbg.generated
     */
    private Integer quantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column activity.coupon_time
     *
     * @mbg.generated
     */
    private LocalDateTime couponTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column activity.end_time
     *
     * @mbg.generated
     */
    private LocalDateTime endTime;
}