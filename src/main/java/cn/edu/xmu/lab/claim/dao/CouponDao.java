package cn.edu.xmu.lab.claim.dao;

import cn.edu.xmu.lab.claim.mapper.ActivityMapper;
import cn.edu.xmu.lab.claim.mapper.CouponMapper;
import cn.edu.xmu.lab.claim.model.Activity;
import cn.edu.xmu.lab.claim.model.Coupon;
import cn.edu.xmu.lab.claim.util.BloomFilterHelper;
import cn.edu.xmu.lab.claim.util.RedisBloomFilter;
import cn.edu.xmu.lab.claim.util.RedisUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

@Repository
public class CouponDao implements InitializingBean {
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    RedisBloomFilter bloom;

    @Override
    public void afterPropertiesSet() throws Exception {
        bloom = new RedisBloomFilter(
                redisTemplate,
                new BloomFilterHelper<>(Funnels.stringFunnel(Charsets.UTF_8), 100000, 0.01));
    }

    private long calcActivityExpire(Activity activity) {
        Duration duration = Duration.between(activity.getCouponTime(), activity.getEndTime());
        long elapse = Math.abs(duration.toSeconds());
        return Math.min(elapse, 450 + (int) (Math.random() * 50.0));
    }

    public Activity getActivity(Integer activityId) {
        Activity activity = (Activity) redisUtil.get("a_" + activityId);
        if (activity == null) {
//            activity = new Activity(1, 10000000, null, null);
//            long expire = 400;
//            List<Coupon> taken = new ArrayList<>(0);
//            selectByPrimaryKey() seems broken
            activity = activityMapper.selectByPrimaryKey(activityId);
            long expire = calcActivityExpire(activity);
            List<Coupon> taken = couponMapper.selectByActivityId(activityId);
            redisUtil.set("a_" + activityId, activity, expire);
            redisUtil.set("an_" + activityId, activity.getQuantity() - taken.size(), expire);
            for (Coupon coupon : taken)
                bloom.addByBloomFilter("au_" + activityId, coupon.getUserId().toString());
        }
        return activity;
    }

    private synchronized boolean claimCoupon(Integer activityId, Integer userId) {
        Integer left = (Integer) redisUtil.get("an_" + activityId);
        if (left > 0) {
            redisUtil.set("an_" + activityId, left - 1, 500);
            bloom.addByBloomFilter("au_" + activityId, userId.toString());
            // MQ: asyncSend save to database
            return true;
        } else {
            return false;
        }
    }

    public Mono<Coupon> tryClaimCoupon(Integer activityId, Integer userId) {
        return Mono.just(new Coupon(activityId, userId)).flatMap(claim -> {
            Integer aid = claim.getActivityId();
            Integer uid = claim.getUserId();
            Activity activity = getActivity(aid);
            // perform time check... stuff like that
            if (bloom.includeByBloomFilter("au_" + aid, uid.toString())) return Mono.justOrEmpty(null);
            boolean success = claimCoupon(aid, uid);
            if (!success) return Mono.justOrEmpty(null);
            return Mono.just(new Coupon(aid, uid));
        });
    }
}
