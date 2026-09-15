package sliit.construction.construction.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public final class DocumentDtos {
 private DocumentDtos() {}
 public record Request(@NotBlank @Size(max=180) String title,@Size(max=60) String documentType,@Size(max=500) String fileUrl,
   @Size(max=500) String filePath,@Size(max=1000) String description,@NotNull Long projectId,@NotNull Long uploadedById) {}
 public record Response(Long id,String title,String documentType,String fileUrl,String filePath,String description,
   Long projectId,Long uploadedById,String uploadedByName,LocalDateTime createdAt,LocalDateTime updatedAt) {}
}
