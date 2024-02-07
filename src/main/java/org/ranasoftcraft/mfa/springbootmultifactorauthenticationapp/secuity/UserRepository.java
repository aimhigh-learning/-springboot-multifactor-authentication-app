package org.ranasoftcraft.mfa.springbootmultifactorauthenticationapp.secuity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sandeep.rana
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

}
