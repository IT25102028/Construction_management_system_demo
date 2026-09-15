package sliit.construction.construction.service;
import sliit.construction.construction.dto.DocumentDtos; import sliit.construction.construction.entity.*; import sliit.construction.construction.repository.*; import sliit.construction.construction.exception.*;
import org.springframework.data.domain.*; import org.springframework.stereotype.Service;
@Service public class DocumentServiceImpl implements DocumentService {
 private final DocumentRepository repo; private final ProjectRepository projects; private final UserRepository users;
 public DocumentServiceImpl(DocumentRepository r,ProjectRepository p,UserRepository u){repo=r;projects=p;users=u;}
 public DocumentDtos.Response create(DocumentDtos.Request r){return map(repo.save(build(new Document(),r)));}
 public Page<DocumentDtos.Response> list(Long projectId,String search,Pageable p){Page<Document> x=projectId!=null?repo.findByProjectId(projectId,p):(search==null||search.isBlank()?repo.findAll(p):repo.findByTitleContainingIgnoreCase(search,p));return x.map(this::map);}
 public DocumentDtos.Response get(Long id){return map(entity(id));}
 public DocumentDtos.Response update(Long id,DocumentDtos.Request r){return map(repo.save(build(entity(id),r)));}
 public void delete(Long id){repo.delete(entity(id));}
 private Document entity(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Document not found: "+id));}
 private Document build(Document x,DocumentDtos.Request r){x.setTitle(r.title());x.setDocumentType(r.documentType());x.setFileUrl(r.fileUrl());x.setFilePath(r.filePath());x.setDescription(r.description());
   x.setProject(projects.findById(r.projectId()).orElseThrow(()->new ResourceNotFoundException("Project not found: "+r.projectId())));
   x.setUploadedBy(users.findById(r.uploadedById()).orElseThrow(()->new ResourceNotFoundException("User not found: "+r.uploadedById())));return x;}
 private DocumentDtos.Response map(Document x){return new DocumentDtos.Response(x.getId(),x.getTitle(),x.getDocumentType(),x.getFileUrl(),x.getFilePath(),x.getDescription(),x.getProject().getId(),x.getUploadedBy().getId(),x.getUploadedBy().getFullName(),x.getCreatedAt(),x.getUpdatedAt());}
}
