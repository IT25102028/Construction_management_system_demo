package sliit.construction.construction.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public final class NotificationDtos {
 private NotificationDtos() {}
 public record Request(@NotBlank @Size(max=180) String title,@NotBlank @Size(max=2000) String message,
   @Size(max=50) String type,@NotNull Long recipientId) {}
 public record UpdateRequest(@NotBlank @Size(max=180) String title,@NotBlank @Size(max=2000) String message,
   @Size(max=50) String type,@NotNull Boolean readFlag,@NotNull Long recipientId) {}
 public record Response(Long id,String title,String message,Boolean readFlag,String type,Long recipientId,LocalDateTime createdAt,LocalDateTime updatedAt) {}
}
