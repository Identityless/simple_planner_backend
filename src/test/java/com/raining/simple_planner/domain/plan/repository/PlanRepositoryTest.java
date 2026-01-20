package com.raining.simple_planner.domain.plan.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.raining.simple_planner.domain.plan.constant.PlanMode;
import com.raining.simple_planner.domain.plan.constant.TimeTableMode;
import com.raining.simple_planner.domain.plan.document.Plan;
import com.raining.simple_planner.domain.plan.dto.PlanAddDateInfoRequestDTO;
import com.raining.simple_planner.domain.plan.dto.PlanRegistrationRequestDTO;
import com.raining.simple_planner.domain.plan.dto.PlanUpdateRequestDTO;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@ActiveProfiles("test")
@DisplayName("Plan Repository CRUD 테스트")
class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Test
    @DisplayName("Plan 생성 - CREATE")
    void testCreatePlan() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<String> memberLoginIds = Arrays.asList("user1", "user2");
        
        PlanRegistrationRequestDTO requestDTO = new PlanRegistrationRequestDTO(
                "test-group-1",
                "팀 회의 일정 조율",
                "팀 전체 회의 일정을 조율하는 플랜입니다",
                now.plusDays(1).toString(),
                now.plusDays(7).toString(),
                now.plusDays(1).withHour(18).toString(),
                PlanMode.AVAILABLE.getCode(),
                TimeTableMode.DATE_TIME.getCode()
        );

        // When
        Plan plan = Plan.initPlan(requestDTO, memberLoginIds);
        Plan savedPlan = planRepository.save(plan);

        // Then
        assertThat(savedPlan.getId()).isNotNull();
        assertThat(savedPlan.getGroupId()).isEqualTo("test-group-1");
        assertThat(savedPlan.getTitle()).isEqualTo("팀 회의 일정 조율");
        assertThat(savedPlan.getPlanMode()).isEqualTo(PlanMode.AVAILABLE);
        assertThat(savedPlan.getTimeTableMode()).isEqualTo(TimeTableMode.DATE_TIME);
        assertThat(savedPlan.getDateTables()).hasSize(2);
        assertThat(savedPlan.getCreatedAt()).isNotNull();
    }

    // @Test
    // @DisplayName("Plan 조회 - READ")
    // void testReadPlan() {
    //     // Given
    //     LocalDateTime now = LocalDateTime.now();
    //     List<String> memberLoginIds = Arrays.asList("user1");
    //     
    //     PlanRegistrationRequestDTO requestDTO = new PlanRegistrationRequestDTO(
    //             "test-group-2",
    //             "프로젝트 킥오프",
    //             "신규 프로젝트 킥오프 일정",
    //             now.toString(),
    //             now.plusDays(5).toString(),
    //             now.plusDays(1).toString(),
    //             PlanMode.UNAVAILABLE.getCode(),
    //             TimeTableMode.ONLY_DATE.getCode()
    //     );
    //
    //     Plan plan = Plan.initPlan(requestDTO, memberLoginIds);
    //     Plan savedPlan = planRepository.save(plan);
    //
    //     // When
    //     Optional<Plan> retrievedPlan = planRepository.findById(savedPlan.getId());
    //
    //     // Then
    //     assertThat(retrievedPlan).isPresent();
    //     assertThat(retrievedPlan.get().getId()).isEqualTo(savedPlan.getId());
    //     assertThat(retrievedPlan.get().getGroupId()).isEqualTo("test-group-2");
    //     assertThat(retrievedPlan.get().getTitle()).isEqualTo("프로젝트 킥오프");
    //     assertThat(retrievedPlan.get().getDescription()).isEqualTo("신규 프로젝트 킥오프 일정");
    //     assertThat(retrievedPlan.get().getPlanMode()).isEqualTo(PlanMode.UNAVAILABLE);
    //     assertThat(retrievedPlan.get().getTimeTableMode()).isEqualTo(TimeTableMode.ONLY_DATE);
    // }

    @Test
    @DisplayName("Plan 업데이트 - UPDATE")
    void testUpdatePlan() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<String> memberLoginIds = Arrays.asList("user1");
        
        PlanRegistrationRequestDTO requestDTO = new PlanRegistrationRequestDTO(
                "test-group-3",
                "원래 제목",
                "원래 설명",
                now.toString(),
                now.plusDays(5).toString(),
                now.plusDays(1).toString(),
                PlanMode.AVAILABLE.getCode(),
                TimeTableMode.DATE_TIME.getCode()
        );

        Plan plan = Plan.initPlan(requestDTO, memberLoginIds);
        Plan savedPlan = planRepository.save(plan);

        // When
        PlanUpdateRequestDTO updateRequestDTO = new PlanUpdateRequestDTO(
                savedPlan.getId(),
                "수정된 제목",
                "수정된 설명",
                now.plusDays(2).toString(),
                now.plusDays(5).toString(),
                now.plusDays(1).toString(),
                PlanMode.AVAILABLE.getCode(),
                TimeTableMode.DATE_TIME.getCode()
        );
        
        savedPlan.updatePlan(updateRequestDTO);
        Plan updatedPlan = planRepository.save(savedPlan);

        // Then
        assertThat(updatedPlan.getId()).isEqualTo(savedPlan.getId());
        assertThat(updatedPlan.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedPlan.getDescription()).isEqualTo("수정된 설명");
        assertThat(updatedPlan.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("Plan 삭제 - DELETE")
    void testDeletePlan() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<String> memberLoginIds = Arrays.asList("user1");
        
        PlanRegistrationRequestDTO requestDTO = new PlanRegistrationRequestDTO(
                "test-group-4",
                "삭제될 플랜",
                "이 플랜은 삭제될 것입니다",
                now.toString(),
                now.plusDays(5).toString(),
                now.plusDays(1).toString(),
                PlanMode.AVAILABLE.getCode(),
                TimeTableMode.DATE_TIME.getCode()
        );

        Plan plan = Plan.initPlan(requestDTO, memberLoginIds);
        Plan savedPlan = planRepository.save(plan);
        String planId = savedPlan.getId();

        // When
        planRepository.deleteById(planId);

        // Then
        Optional<Plan> deletedPlan = planRepository.findById(planId);
        assertThat(deletedPlan).isEmpty();
    }

    // @Test
    // @DisplayName("Plan의 dataTables 필드 매핑 확인")
    // void testDateTablesMapping() {
    //     // Given
    //     LocalDateTime now = LocalDateTime.now();
    //     List<String> memberLoginIds = Arrays.asList("user1", "user2", "user3");
    //     
    //     PlanRegistrationRequestDTO requestDTO = new PlanRegistrationRequestDTO(
    //             "test-group-5",
    //             "다중 사용자 플랜",
    //             "여러 사용자가 참여하는 플랜",
    //             now.toString(),
    //             now.plusDays(7).toString(),
    //             now.plusDays(1).toString(),
    //             PlanMode.AVAILABLE.getCode(),
    //             TimeTableMode.DATE_TIME.getCode()
    //     );
    //
    //     Plan plan = Plan.initPlan(requestDTO, memberLoginIds);
    //     Plan savedPlan = planRepository.save(plan);
    //
    //     // When
    //     Optional<Plan> retrievedPlan = planRepository.findById(savedPlan.getId());
    //
    //     // Then
    //     assertThat(retrievedPlan).isPresent();
    //     assertThat(retrievedPlan.get().getDateTables()).hasSize(3);
    //     assertThat(retrievedPlan.get().getDateTables()).containsKeys("user1", "user2", "user3");
    //     assertThat(retrievedPlan.get().getDateTables().get("user1")).isNotNull();
    //     assertThat(retrievedPlan.get().getDateTables().get("user1").getTimeTables()).isEmpty();
    // }

    @Test
    @DisplayName("Plan의 중첩 객체 addDateInfo 메서드 동작 확인")
    void testAddDateInfoMapping() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<String> memberLoginIds = Arrays.asList("user1", "user2");
        
        PlanRegistrationRequestDTO requestDTO = new PlanRegistrationRequestDTO(
                "test-group-6",
                "날짜 정보 추가 테스트",
                "날짜 정보를 추가하는 테스트",
                now.toString(),
                now.plusDays(7).toString(),
                now.plusDays(1).toString(),
                PlanMode.AVAILABLE.getCode(),
                TimeTableMode.DATE_TIME.getCode()
        );

        Plan plan = Plan.initPlan(requestDTO, memberLoginIds);
        Plan savedPlan = planRepository.save(plan);

        // When
        // user1에 여러 시간 정보 추가
        PlanAddDateInfoRequestDTO dateInfoDTO1 = new PlanAddDateInfoRequestDTO(
                null,
                now.plusDays(2).withHour(14),
                now.plusDays(2).withHour(15)
        );
        savedPlan.addDateInfo("user1", dateInfoDTO1);
        
        PlanAddDateInfoRequestDTO dateInfoDTO2 = new PlanAddDateInfoRequestDTO(
                null,
                now.plusDays(2).withHour(16),
                now.plusDays(2).withHour(17)
        );
        savedPlan.addDateInfo("user1", dateInfoDTO2);
        
        // user2에 시간 정보 추가
        PlanAddDateInfoRequestDTO dateInfoDTO3 = new PlanAddDateInfoRequestDTO(
                null,
                now.plusDays(3).withHour(10),
                now.plusDays(3).withHour(11)
        );
        savedPlan.addDateInfo("user2", dateInfoDTO3);
        
        Plan updatedPlan = planRepository.save(savedPlan);

        // Then - 메모리상의 객체 검증 (TimeSlot까지 확인)
        assertThat(updatedPlan.getDateTables().get("user1")).isNotNull();
        assertThat(updatedPlan.getDateTables().get("user1").getTimeTables()).isNotEmpty();
        assertThat(updatedPlan.getDateTables().get("user1").getTimeTables())
                .containsKey(now.plusDays(2).toLocalDate());
        
        // user1의 2월2일 TimeSlots 확인
        assertThat(updatedPlan.getDateTables().get("user1").getTimeTables()
                .get(now.plusDays(2).toLocalDate()).getTimeSlots()).hasSize(2);
        
        // user2의 데이터 확인
        assertThat(updatedPlan.getDateTables().get("user2")).isNotNull();
        assertThat(updatedPlan.getDateTables().get("user2").getTimeTables()).isNotEmpty();
        assertThat(updatedPlan.getDateTables().get("user2").getTimeTables())
                .containsKey(now.plusDays(3).toLocalDate());
        assertThat(updatedPlan.getDateTables().get("user2").getTimeTables()
                .get(now.plusDays(3).toLocalDate()).getTimeSlots()).hasSize(1);
    }

    // @Test
    // @DisplayName("Plan 전체 조회")
    // void testFindAllPlans() {
    //     // Given
    //     LocalDateTime now = LocalDateTime.now();
    //     List<String> memberLoginIds = Arrays.asList("user1");
    //
    //     PlanRegistrationRequestDTO requestDTO1 = new PlanRegistrationRequestDTO(
    //             "group-1",
    //             "플랜 1",
    //             "설명 1",
    //             now.toString(),
    //             now.plusDays(5).toString(),
    //             now.plusDays(1).toString(),
    //             PlanMode.AVAILABLE.getCode(),
    //             TimeTableMode.DATE_TIME.getCode()
    //     );
    //
    //     PlanRegistrationRequestDTO requestDTO2 = new PlanRegistrationRequestDTO(
    //             "group-2",
    //             "플랜 2",
    //             "설명 2",
    //             now.toString(),
    //             now.plusDays(5).toString(),
    //             now.plusDays(1).toString(),
    //             PlanMode.UNAVAILABLE.getCode(),
    //             TimeTableMode.ONLY_DATE.getCode()
    //     );
    //
    //     Plan plan1 = Plan.initPlan(requestDTO1, memberLoginIds);
    //     Plan plan2 = Plan.initPlan(requestDTO2, memberLoginIds);
    //     
    //     planRepository.save(plan1);
    //     planRepository.save(plan2);
    //
    //     // When
    //     int totalPlans = (int) planRepository.count();
    //
    //     // Then
    //     assertThat(totalPlans).isGreaterThanOrEqualTo(2);
    // }

    // @Test
    // @DisplayName("Plan의 불변성 필드 final 필드 매핑 테스트")
    // void testFinalFieldMapping() {
    //     // Given
    //     LocalDateTime now = LocalDateTime.now();
    //     List<String> memberLoginIds = Arrays.asList("user1");
    //     
    //     PlanRegistrationRequestDTO requestDTO = new PlanRegistrationRequestDTO(
    //             "test-group-7",
    //             "final 필드 테스트",
    //             "groupId가 final인지 확인",
    //             now.toString(),
    //             now.plusDays(5).toString(),
    //             now.plusDays(1).toString(),
    //             PlanMode.AVAILABLE.getCode(),
    //             TimeTableMode.DATE_TIME.getCode()
    //     );
    //
    //     Plan plan = Plan.initPlan(requestDTO, memberLoginIds);
    //     Plan savedPlan = planRepository.save(plan);
    //
    //     // When
    //     Optional<Plan> retrievedPlan = planRepository.findById(savedPlan.getId());
    //
    //     // Then
    //     assertThat(retrievedPlan).isPresent();
    //     // groupId가 정상적으로 매핑되었는지 확인
    //     assertThat(retrievedPlan.get().getGroupId()).isEqualTo("test-group-7");
    //     // dateTables가 정상적으로 매핑되었는지 확인
    //     assertThat(retrievedPlan.get().getDateTables()).hasSize(1);
    //     assertThat(retrievedPlan.get().getDateTables()).containsKey("user1");
    // }
}
