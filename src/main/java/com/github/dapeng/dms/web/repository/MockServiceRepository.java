package com.github.dapeng.dms.web.repository;

import com.github.dapeng.dms.web.entity.MockService;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:52 PM
 */
public interface MockServiceRepository extends BaseRepository<MockService> {


    MockService findByServiceName(String name);

    MockService findById(long id);


}
