package sliit.construction.construction.repository;
import sliit.construction.construction.entity.Notification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);
    Page<Notification> findByRecipientIdAndReadFlag(Long recipientId, Boolean readFlag, Pageable pageable);
}
