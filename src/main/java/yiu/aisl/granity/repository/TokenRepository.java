package yiu.aisl.granity.repository;

import yiu.aisl.granity.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
