package za.co.sanlam.banking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.sanlam.banking_service.model.Account;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
}
