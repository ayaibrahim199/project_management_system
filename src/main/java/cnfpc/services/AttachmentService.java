package cnfpc.services;
import org.springframework.stereotype.Service;
import cnfpc.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import cnfpc.entities.Attachments;
import java.util.List;
import java.util.Optional;
@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    public List<Attachments> getAttachmentsByTaskId(Long taskId) {
        return attachmentRepository.findByTaskId(taskId);
    }

    public Optional<Attachments> getAttachmentById(Long id) {
        return attachmentRepository.findById(id);
    }

    public Attachments saveAttachment(Attachments attachment) {
        return attachmentRepository.save(attachment);
    }

    public void deleteAttachment(Long id) {
        attachmentRepository.deleteById(id);
    }
}