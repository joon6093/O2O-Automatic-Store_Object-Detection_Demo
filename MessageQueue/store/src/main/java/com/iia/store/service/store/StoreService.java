package com.iia.store.service.store;

import com.iia.store.dto.image.ImageDto;
import com.iia.store.dto.store.*;
import com.iia.store.entity.member.Member;
import com.iia.store.entity.store.Store;
import com.iia.store.entity.store.StoreImage;
import com.iia.store.repository.member.MemberRepository;
import com.iia.store.repository.store.StoreRepository;
import com.iia.store.service.image.ImageService;
import com.iia.store.config.exception.MemberNotFoundException;
import com.iia.store.config.exception.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Transactional
    public StoreCreateResponse create(StoreCreateRequest req) {
        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        StoreImage storeImage = StoreImage.builder()
                .originName(req.getImage().getOriginalFilename())
                .build();

        Store store = Store.builder()
                .name(req.getName())
                .description(req.getDescription())
                .image(storeImage)
                .member(member)
                .build();

        storeRepository.save(store);
        uploadImage(req.getImage(), store.getImage());
        return new StoreCreateResponse(store.getId());
    }

    private void uploadImage(MultipartFile fileImages, StoreImage image) {
        imageService.upload(fileImages, image.getUniqueName());
    }

    @Transactional
    public void delete(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);
        deleteImage(store.getImage());
        storeRepository.delete(store);
    }

    private void deleteImage(StoreImage image) {
        imageService.delete(image.getUniqueName());
    }

    public StoreListDto readByMember(Long id) {
        List<Store> stores = storeRepository.findByMemberId(id);
        List<StoreSimpleDto> storeDtos = stores.stream()
                .map(store -> new StoreSimpleDto(
                        store.getId(),
                        store.getName(),
                        store.getDescription(),
                        store.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new StoreListDto(storeDtos);
    }

    public StoreListDto readAll() {
        List<Store> stores = storeRepository.findAll();
        List<StoreSimpleDto> storeSimpleDtos = stores.stream()
                .map(store -> new StoreSimpleDto(
                        store.getId(),
                        store.getName(),
                        store.getDescription(),
                        store.getCreatedAt()
                ))
                .collect(Collectors.toList());
        return new StoreListDto(storeSimpleDtos);
    }

    public StoreDto read(Long id) {
        Store store = storeRepository.findByIdWithMemberAndImage(id).orElseThrow(StoreNotFoundException::new);
        return new StoreDto(store.getId(), store.getName(), store.getDescription(),
                new ImageDto(store.getImage().getId(), store.getImage().getOriginName(),
                        store.getImage().getUniqueName()), store.getCreatedAt(), store.getModifiedAt());
    }
}
