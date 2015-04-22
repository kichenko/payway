/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SideBarMenu of admin webapp
 *
 * @author Sergey Kichenko
 * @created 21.04.15 00:00
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class SideBarMenuItemClickBusEvent implements AdminBusEvent {

    private String tag;
}
