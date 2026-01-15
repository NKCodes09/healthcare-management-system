package controller;

import model.Referral;
import repository.ReferralManager;
import repository.ReferralRepository;

import java.io.IOException;
import java.util.List;

public class ReferralController {

    private final ReferralRepository repository = new ReferralRepository();
    private final ReferralManager manager = ReferralManager.getInstance();

    public List<Referral> getAll() {
        return repository.getAll();
    }

    public void add(Referral r) throws IOException {
        manager.processReferral(r, repository); // CSV + TXT
    }

    public void update(int index, Referral r) throws IOException {
        repository.getAll().set(index, r);
        repository.updateAll();
    }

    public void delete(int index) throws IOException {
        repository.deleteReferral(index);
    }
}
