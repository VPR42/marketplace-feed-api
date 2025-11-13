package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.repository.CategoryRepository;
import com.vpr42.marketplacefeedapi.repository.TagsRepository;
import com.vpr42.marketplacefeedapi.service.StartupInitializerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartupInitializerServiceImpl implements StartupInitializerService {
    private final CategoryRepository categoryRepository;
    private final TagsRepository tagsRepository;

    @Override
    @PostConstruct
    @Transactional
    public void initialize() {
        log.info("Initializing startup data...");

        if (categoryRepository.count() > 0) {
            log.info("Data already initialized");
            return;
        }

        List<String> categoriesName = List.of(
                "Электроника",
                "Уборка",
                "Мелкий ремонт",
                "Сантехника",
                "IT-услуги",
                "Кондиционеры",
                "Сборка мебели",
                "Монтажные работы",
                "Ремонт",
                "Автомобили"
        );
        List<String> tagNames = List.of(
                "Экологичная химия",
                "Мытьё окон",
                "Своё оборудование",
                "Гарантия",
                "Срочный вызов",
                "24/7",
                "После ремонта",
                "Химчистка",
                "Безнал",
                "Наличные",
                "Выезд сегодня",
                "Чек и договор"
        );
        List<CategoryEntity> startupCategoriesData = categoriesName.stream()
                .map(category -> CategoryEntity
                        .builder()
                        .name(category)
                        .build())
                .toList();
        List<TagEntity> startupTagEntitiesData = tagNames.stream()
                .map(tag -> TagEntity
                        .builder()
                        .name(tag)
                        .build())
                .toList();

        try {
            categoryRepository.saveAll(startupCategoriesData);
            tagsRepository.saveAll(startupTagEntitiesData);
            log.info("Data successfully initialized");
        } catch (Exception e) {
            log.error("Failed to initialize data", e);
        }
    }
}
