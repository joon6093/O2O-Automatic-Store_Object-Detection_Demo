package com.iia.store.service.store;

import com.iia.store.config.exception.*;
import com.iia.store.config.tocken.TokenHandler;
import com.iia.store.dto.image.ImageDto;
import com.iia.store.dto.store.*;
import com.iia.store.entity.member.Member;
import com.iia.store.entity.role.Role;
import com.iia.store.entity.role.RoleType;
import com.iia.store.entity.store.Store;
import com.iia.store.entity.store.StoreImage;
import com.iia.store.repository.member.MemberRepository;
import com.iia.store.repository.role.RoleRepository;
import com.iia.store.repository.store.StoreRepository;
import com.iia.store.service.image.ImageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final ImageService imageService;
    private final TokenHandler storeAccessTokenHandler;
    public StoreService(StoreRepository storeRepository, MemberRepository memberRepository, ImageService imageService, RoleRepository roleRepository,
                        @Qualifier("storeAccessTokenHandler")TokenHandler storeAccessTokenHandler) {
        this.storeRepository = storeRepository;
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.imageService = imageService;
        this.storeAccessTokenHandler = storeAccessTokenHandler;
    }

    @Transactional
    public StoreCreateResponse create(StoreCreateRequest req) {
        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Role role = roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new);
        StoreImage storeImage = StoreImage.builder()
                .originName(req.getImage().getOriginalFilename())
                .build();

        Store store = Store.builder()
                .name(req.getName())
                .description(req.getDescription())
                .image(storeImage)
                .member(member)
                .roles(List.of(role))
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

    public StoreListDto readBySignIn(Long id) {
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

    public StoreSelectResponse storeSelect(StoreSelectRequest req) {
        Store store = storeRepository.findByIdAndMemberId(req.getStoreId(), req.getMemberId())
                .orElseThrow(SelectStoreFailureException::new);
        TokenHandler.PrivateClaims privateClaims = createPrivateClaims(store);
        String storeAccessToken = storeAccessTokenHandler.createToken(privateClaims);
        return new StoreSelectResponse(storeAccessToken);
    }

    private TokenHandler.PrivateClaims createPrivateClaims(Store store) {
        return new TokenHandler.PrivateClaims(
                String.valueOf(store.getId()),
                store.getRoles().stream()
                        .map(storeRole -> storeRole.getRole())
                        .map(role -> role.getRoleType())
                        .map(roleType -> roleType.toString())
                        .collect(Collectors.toList()));
    }

    public StoreRefreshTokenResponse refreshStoreToken (StoreRefreshTokenRequest req) {
        TokenHandler.PrivateClaims privateClaims = storeAccessTokenHandler.parseExpiredToken(req.getExpiredStoreToken()).orElseThrow(RefreshTokenFailureException::new);
        String accessToken = storeAccessTokenHandler.createToken(privateClaims);
        return new StoreRefreshTokenResponse(accessToken);
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
