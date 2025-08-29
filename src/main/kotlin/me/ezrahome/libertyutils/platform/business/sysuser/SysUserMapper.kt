package me.ezrahome.libertyutils.platform.business.sysuser

import com.okta.sdk.resource.user.User
import me.ezrahome.libertyutils.configuration.mapping.MapperConfig
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.util.UUID
import java.util.function.Supplier

@Mapper(config = MapperConfig::class)
interface SysUserMapper {
    @Mapping(source = "id", target = "oktaId")
    @Mapping(target = "id", expression = "java(idSupplier.get())")
    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    @Mapping(source = "profile.mobilePhone", target = "mobilePhone")
    @Mapping(source = "profile.secondEmail", target = "secondEmail")
    @Mapping(source = "profile.email", target = "email")
    fun oktaToSystemUser(oktaUserDto: User?, @Context idSupplier: Supplier<UUID?>): SysUserDto
}
