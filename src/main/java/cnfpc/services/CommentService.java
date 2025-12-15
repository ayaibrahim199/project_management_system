package cnfpc.services;
import org.springframework.stereotype.Service;
import cnfpc.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import cnfpc.entities.Comments;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comments> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Optional<Comments> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Comments saveComment(Comments comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}