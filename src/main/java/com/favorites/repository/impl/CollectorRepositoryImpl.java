package com.favorites.repository.impl;

import com.favorites.domain.view.CollectorView;
import com.favorites.repository.BaseNativeSqlRepository;
import com.favorites.repository.CollectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 获取收藏家
 * @Auth: yuyang
 * @Date: 2017/1/18 19:35
 * @Version: 1.0
 **/
@Service
public class CollectorRepositoryImpl extends BaseNativeSqlRepository implements CollectorRepository {


    @Override
    public Long getMostCollectUser() {
        String querySql="SELECT c.user_id ,COUNT(1) AS counts FROM collect c GROUP BY c.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> objecArraytList = sqlArrayList(querySql);
        Object[] obj =  objecArraytList.get(0);
        return Long.valueOf(obj[0].toString());
    }

    @Override
    public Long getMostFollowedUser() {
        String querysql="SELECT id,follow_id as user_id,COUNT(1) AS counts FROM follow GROUP BY follow_id ORDER BY counts DESC LIMIT 1";
        CollectorView cv = new CollectorView();
        List<CollectorView> list = sqlObjectList(querysql,cv);
        Long userId = list.get(0).getUserId();
        return  userId;
    }

    @Override
    public Long getMostPraisedUser() {
        String querySql="SELECT c.user_id,SUM(p.counts) as counts FROM collect c LEFT JOIN \n" +
                "(SELECT collect_id,COUNT(1) as counts FROM praise GROUP BY collect_id)p \n" +
                "ON c.id=p.collect_id GROUP BY c.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> objecArraytList = sqlArrayList(querySql);
        Object[] obj =  objecArraytList.get(0);
        return Long.valueOf(obj[0].toString());
    }

    @Override
    public Long getMostCommentedUser() {
        String querySql="SELECT c.user_id,SUM(p.counts) as counts FROM collect c LEFT JOIN \n" +
                "(SELECT collect_id,COUNT(1) as counts FROM `comment` GROUP BY collect_id)p \n" +
                "ON c.id=p.collect_id GROUP BY c.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> objecArraytList = sqlArrayList(querySql);
        Object[] obj =  objecArraytList.get(0);
        return Long.valueOf(obj[0].toString());
    }

    @Override
    public Long getMostPopularUser() {
        String querySql="SELECT u.user_id,SUM(u.counts) as counts FROM\n" +
                "(SELECT user_id,COUNT(1) as counts FROM notice GROUP BY user_id\n" +
                "UNION ALL\n" +
                "SELECT follow_id,COUNT(1) AS counts FROM follow GROUP BY follow_id)u\n" +
                "GROUP BY u.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> objecArraytList = sqlArrayList(querySql);
        Object[] obj =  objecArraytList.get(0);
        return Long.valueOf(obj[0].toString());
    }

    @Override
    public Long getMostActiveUser() {
        String querySql="SELECT u.user_id,SUM(u.counts) as counts FROM\n" +
                "(SELECT user_id,COUNT(1) as counts FROM collect WHERE create_time>1482076800000 AND create_time<1484731170155 GROUP BY user_id\n" +
                "UNION ALL\n" +
                "SELECT user_id,COUNT(1) as counts FROM `comment` WHERE create_time>1482076800000 AND create_time<1484731170155 GROUP BY user_id\n" +
                "UNION ALL\n" +
                "SELECT user_id,COUNT(1) as counts FROM praise WHERE create_time>1482076800000 AND create_time<1484731170155 GROUP BY user_id\n" +
                "UNION ALL\n" +
                "SELECT user_id,COUNT(1) as counts FROM follow WHERE create_time>1482076800000 AND create_time<1484731170155 GROUP BY user_id)u\n" +
                "GROUP BY u.user_id ORDER BY counts DESC LIMIT 1";
        List<Object[]> objecArraytList = sqlArrayList(querySql);
        Object[] obj =  objecArraytList.get(0);
        return Long.valueOf(obj[0].toString());
    }
}
