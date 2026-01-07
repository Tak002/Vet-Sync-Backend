package com.vetsync.backend.repository;

import com.vetsync.backend.domain.TaskDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskDefinitionRepository  extends JpaRepository<TaskDefinition, UUID> {
    // 정렬 조회: fixed=true는 order 오름차순, 그 후 fixed=false는 name 오름차순
    List<TaskDefinition> findAllByHospital_IdOrderByIsFixedDescOrderAscNameAsc(UUID hospitalId);

    Boolean existsByIdAndHospital_Id(UUID id, UUID hospitalId);

    boolean existsByHospital_IdAndNameIgnoreCase(UUID hospitalId, String name);

    boolean existsByHospital_IdAndNameIgnoreCaseAndIdNot(UUID hospitalId, String name, UUID id);

    // 최대 order 조회 (fixed=true만)
    @Query("select coalesce(max(t.order), 0) from TaskDefinition t where t.hospital.id = :hospitalId and t.isFixed = true")
    Integer getMaxOrder(@Param("hospitalId") UUID hospitalId);

    // 순차 업데이트용 조회: X < o <= Y 구간 오름차순(자기 자신 제외)
    @Query("select t from TaskDefinition t where t.hospital.id = :hospitalId and t.isFixed = true and t.id <> :excludeId and t.order between :start and :end order by t.order asc")
    List<TaskDefinition> findRangeAscExcludeSelf(@Param("hospitalId") UUID hospitalId,
                                                 @Param("start") int start,
                                                 @Param("end") int end,
                                                 @Param("excludeId") UUID excludeId);

    // 순차 업데이트용 조회: Y <= o < X 구간 내림차순(자기 자신 제외)
    @Query("select t from TaskDefinition t where t.hospital.id = :hospitalId and t.isFixed = true and t.id <> :excludeId and t.order between :start and :end order by t.order desc")
    List<TaskDefinition> findRangeDescExcludeSelf(@Param("hospitalId") UUID hospitalId,
                                                  @Param("start") int start,
                                                  @Param("end") int end,
                                                  @Param("excludeId") UUID excludeId);

    // 순차 업데이트용 조회(자기 자신 제외 없음): start <= o <= end 오름차순
    @Query("select t from TaskDefinition t where t.hospital.id = :hospitalId and t.isFixed = true and t.order between :start and :end order by t.order asc")
    List<TaskDefinition> findRangeAsc(@Param("hospitalId") UUID hospitalId,
                                      @Param("start") int start,
                                      @Param("end") int end);

    // 순차 업데이트용 조회(자기 자신 제외 없음): start <= o <= end 내림차순
    @Query("select t from TaskDefinition t where t.hospital.id = :hospitalId and t.isFixed = true and t.order between :start and :end order by t.order desc")
    List<TaskDefinition> findRangeDesc(@Param("hospitalId") UUID hospitalId,
                                       @Param("start") int start,
                                       @Param("end") int end);
}
