package com.github.dapeng.dms.web.repository;

import com.github.dapeng.dms.web.entity.MockMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:52 PM
 */
public interface MetadataRepository extends JpaRepository<MockMetadata, Long> {

    List<MockMetadata> findByServiceId(long serviceId);
}
