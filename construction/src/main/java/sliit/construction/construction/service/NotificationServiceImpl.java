package sliit.construction.construction.service;
import sliit.construction.construction.dto.NotificationDtos; import sliit.construction.construction.entity.*; import sliit.construction.construction.repository.*; import sliit.construction.construction.exception.*;
import org.springframework.data.domain.*; import org.springframework.stereotype.Service;
@Service public class NotificationServiceImpl implements NotificationService {
 private final NotificationRepository repo; private final UserRepository users; public NotificationServiceImpl(NotificationRepository r,UserRepository u){repo=r;users=u;}
 public NotificationDtos.Response create(NotificationDtos.Request r){Notification n=Notification.builder().title(r.title()).message(r.message()).type(r.type()).readFlag(false).recipient(user(r.recipientId())).build();return map(repo.save(n));}
 public Page<NotificationDtos.Response> list(Long rid,Boolean read,Pageable p){Page<Notification> x=read!=null&&rid!=null?repo.findByRecipientIdAndReadFlag(rid,read,p):(rid!=null?repo.findByRecipientId(rid,p):repo.findAll(p));return x.map(this::map);}
 public NotificationDtos.Response get(Long id){return map(entity(id));}
 public NotificationDtos.Response update(Long id,NotificationDtos.UpdateRequest r){Notification n=entity(id);n.setTitle(r.title());n.setMessage(r.message());n.setType(r.type());n.setReadFlag(r.readFlag());n.setRecipient(user(r.recipientId()));return map(repo.save(n));}
 public void delete(Long id){repo.delete(entity(id));}
 private Notification entity(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Notification not found: "+id));}
 private User user(Long id){return users.findById(id).orElseThrow(()->new ResourceNotFoundException("Recipient not found: "+id));}
 private NotificationDtos.Response map(Notification n){return new NotificationDtos.Response(n.getId(),n.getTitle(),n.getMessage(),n.getReadFlag(),n.getType(),n.getRecipient().getId(),n.getCreatedAt(),n.getUpdatedAt());}
}
