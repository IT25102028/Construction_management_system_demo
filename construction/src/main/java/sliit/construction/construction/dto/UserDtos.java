package sliit.construction.construction.dto;
import sliit.construction.construction.entity.Role;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public final class UserDtos {
 private UserDtos() {}
 public record Request(@NotBlank @Size(max=50) String username,@NotBlank @Email @Size(max=120) String email,
   @Size(min=8,max=100) String password,@NotNull Role role,@NotBlank @Size(max=120) String fullName,@Size(max=30) String phoneNumber) {}
 public record Response(Long id,String username,String email,Role role,String fullName,String phoneNumber,LocalDateTime createdAt,LocalDateTime updatedAt) {}
}
