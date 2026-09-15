package sliit.construction.construction.repository;
import sliit.construction.construction.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MaterialRepository extends JpaRepository<Material,Long> {
    Page<Material> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Material> findByDeliveryStatus(DeliveryStatus status, Pageable pageable);
}
