package cn.edu.xmu.lab.claim.service;

import cn.edu.xmu.lab.claim.dao.CouponDao;
import cn.edu.xmu.lab.claim.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CouponService {
    @Autowired
    private CouponDao couponDao;

    public Mono<Coupon> claimCoupon(Integer activityId, Integer userId) {
        return couponDao.tryClaimCoupon(activityId, userId);
    }
}
