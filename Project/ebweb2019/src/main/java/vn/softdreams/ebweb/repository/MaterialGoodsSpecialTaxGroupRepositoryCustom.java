package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecialTaxGroup;

import java.util.UUID;

@Repository
public interface MaterialGoodsSpecialTaxGroupRepositoryCustom {
    Page<MaterialGoodsSpecialTaxGroup> pageableAllMaterialGoodsSpecialTaxGroup(Pageable sort, UUID org);
}

