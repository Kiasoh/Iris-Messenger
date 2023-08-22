package ir.mohaymen.iris.search.contact;

import java.util.List;

public interface SearchContactService {

    String index(SearchContactDto contact);

    List<String> bulkIndex(List<SearchContactDto> contacts);

    void deleteById(Long id);

    List<SearchContactDto> searchByName(String Name, Long userId);
}
