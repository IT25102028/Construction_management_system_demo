package sliit.construction.construction.service;
import sliit.construction.construction.dto.UserDtos; import sliit.construction.construction.entity.User; import sliit.construction.construction.repository.UserRepository; import sliit.construction.construction.exception.*;
import org.springframework.data.domain.*; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service;
@Service("userService")
public class UserServiceImpl implements UserService {
 private final UserRepository repo; private final PasswordEncoder encoder;
 public UserServiceImpl(UserRepository repo,PasswordEncoder encoder){this.repo=repo;this.encoder=encoder;}
 public UserDtos.Response create(UserDtos.Request r){ if(repo.existsByUsername(r.username()))throw new DuplicateResourceException("Username already exists"); if(repo.existsByEmail(r.email()))throw new DuplicateResourceException("Email already exists");
   User u=User.builder().username(r.username()).email(r.email()).passwordHash(encoder.encode(r.password())).role(r.role()).fullName(r.fullName()).phoneNumber(r.phoneNumber()).build(); return map(repo.save(u));}
 public Page<UserDtos.Response> list(String s,Pageable p){return (s==null||s.isBlank()?repo.findAll(p):repo.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(s,s,p)).map(this::map);}
 public UserDtos.Response get(Long id){return map(getEntity(id));}
 public UserDtos.Response update(Long id,UserDtos.Request r){User u=getEntity(id); if(!u.getUsername().equals(r.username())&&repo.existsByUsername(r.username()))throw new DuplicateResourceException("Username already exists"); if(!u.getEmail().equals(r.email())&&repo.existsByEmail(r.email()))throw new DuplicateResourceException("Email already exists");
   u.setUsername(r.username());u.setEmail(r.email());if(r.password()!=null&&!r.password().isBlank())u.setPasswordHash(encoder.encode(r.password()));u.setRole(r.role());u.setFullName(r.fullName());u.setPhoneNumber(r.phoneNumber());return map(repo.save(u));}
 public void delete(Long id){repo.delete(getEntity(id));}
 public User getEntity(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found: "+id));}
 public User getByUsername(String username){return repo.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found"));}
 private UserDtos.Response map(User u){return new UserDtos.Response(u.getId(),u.getUsername(),u.getEmail(),u.getRole(),u.getFullName(),u.getPhoneNumber(),u.getCreatedAt(),u.getUpdatedAt());}
}
