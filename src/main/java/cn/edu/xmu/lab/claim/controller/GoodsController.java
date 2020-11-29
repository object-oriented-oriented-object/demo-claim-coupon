package cn.edu.xmu.lab.claim.controller;

import cn.edu.xmu.lab.claim.model.Activity;
import cn.edu.xmu.lab.claim.model.Coupon;
import cn.edu.xmu.lab.claim.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class GoodsController {
    @Autowired
    CouponService couponService;

    @GetMapping(path = "/activities/{activityId}/user/{userId}")
    public Mono<Coupon> claimCoupon(@PathVariable Integer activityId, @PathVariable Integer userId) {
        return couponService.claimCoupon(activityId, userId);
    }

}
