package cs204.project.Exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class ResultsNotFoundException extends RuntimeException{
  private static final long serialVersionUID = 4L;

  public PlayerNotFoundException(Long id) {
        super("Could not find Player " + id);
    }
}
