package cn.edu.xmu.lab.claim.model;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Coupon implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon.activity_id
     *
     * @mbg.generated
     */
    @NonNull
    private Integer activityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon.user_id
     *
     * @mbg.generated
     */
    @NonNull
    private Integer userId;

}