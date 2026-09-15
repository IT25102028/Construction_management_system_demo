package sliit.construction.construction.dto;
import sliit.construction.construction.entity.DeliveryStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public final class MaterialDtos {
 private MaterialDtos() {}
 public record Request(@NotBlank @Size(max=150) String name,@NotNull @PositiveOrZero BigDecimal quantity,@NotBlank @Size(max=30) String unit,
   @Size(max=150) String supplier,@NotNull @PositiveOrZero BigDecimal stockThreshold,@Size(max=1000) String purchaseRequest,
   @NotNull DeliveryStatus deliveryStatus) {}
 public record Response(Long id,String name,BigDecimal quantity,String unit,String supplier,BigDecimal stockThreshold,String purchaseRequest,
   DeliveryStatus deliveryStatus,LocalDateTime createdAt,LocalDateTime updatedAt) {}
}
