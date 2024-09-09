package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Priority;
import com.sails.client_connect.entity.Status;
import com.sails.client_connect.entity.Task;
import com.sails.client_connect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * @param id   task id
     * @param user Logged in user
     * @return Task that matches task id and user
     */
    Optional<Task> findByIdAndUser(Long id, User user);

    /**
     * @param user
     * @return Task that matches user
     */
    List<Task> findByUser(User user);

    //Query to Search based on query string and userID
    @Query("SELECT t FROM Task t WHERE " +
            "(LOWER(t.clientName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.taskTitle) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "t.status = :status OR " +
            "t.priority = :priority) AND " +
            "t.user.user_id = :userId")
    Page<Task> searchTasksByUser(@Param("query") String query,
                                 @Param("status") Status status,
                                 @Param("priority") Priority priority,
                                 @Param("userId") Long userId,
                                 Pageable pageable);

    //Query method to filter and sort tasks by various criteria.
    @Query("SELECT t FROM Task t WHERE " +
            "(:id IS NULL OR t.id = :id) AND " +
            "(:clientName IS NULL OR LOWER(t.clientName) LIKE LOWER(CONCAT('%', :clientName, '%'))) AND " +
            "(:taskTitle IS NULL OR LOWER(t.taskTitle) LIKE LOWER(CONCAT('%', :taskTitle, '%'))) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:dueDateTime IS NULL OR t.dueDateTime = :dueDateTime) AND " +
            "t.user.user_id = :userId")
    Page<Task> filterAndSortTasksByUser(@Param("id") Long id,
                                        @Param("clientName") String clientName,
                                        @Param("taskTitle") String taskTitle,
                                        @Param("status") String status,
                                        @Param("priority") String priority,
                                        @Param("dueDateTime") LocalDateTime dueDateTime,
                                        Pageable pageable, @Param("userId") Long userId);


}



