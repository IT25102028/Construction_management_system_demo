package sliit.construction.construction.service;
import sliit.construction.construction.dto.UserDtos;
import sliit.construction.construction.entity.User;
import org.springframework.data.domain.*;
public interface UserService {
 UserDtos.Response create(UserDtos.Request request);
 Page<UserDtos.Response> list(String search, Pageable pageable);
 UserDtos.Response get(Long id);
 UserDtos.Response update(Long id, UserDtos.Request request);
 void delete(Long id);
 User getEntity(Long id);
 User getByUsername(String username);
}
