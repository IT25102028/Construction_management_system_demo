package sliit.construction.construction.service;
import sliit.construction.construction.dto.NotificationDtos;
import org.springframework.data.domain.*;
public interface NotificationService {
 NotificationDtos.Response create(NotificationDtos.Request r); Page<NotificationDtos.Response> list(Long recipientId,Boolean read,Pageable p);
 NotificationDtos.Response get(Long id); NotificationDtos.Response update(Long id,NotificationDtos.UpdateRequest r); void delete(Long id);
}
