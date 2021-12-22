package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.models.dto.UserTestDto;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

//Пример какой-нибудь реализации для конкретной пагинации. Название бина необходимо для того, чтобы помещать его в контроллере в мапу с параметрами
@Repository("AllUser")
public class GetAllUserImpl implements PaginationDtoAble<UserTestDto> {

    @Override
    public List<UserTestDto> getItems(Map<String, Object> param) {
        //Какая-то логика работы с БД
        return null;
    }

    @Override
    public int getTotalResultCount(Map<String, Object> param) {
        //Какая-то логика работы с БД
        return 0;
    }
}
