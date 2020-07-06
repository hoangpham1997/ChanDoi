import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from 'app/core/auth/principal.service';
import any = jasmine.any;

/**
 * @whatItDoes Conditionally includes an HTML element if current user has some
 * of the authorities passed as the `expression` or other conditions.
 *
 * @howToUse
 * ``` conditions like *ngIf
 *      Tham số là mảng các role và Condition mà tất cả các điều kiện đúng sẽ trả về true
 *      Hiển thị element khi 1 trong số các tham số true
 *     <some-element *ebHasSomeAuthorityAndCondition="[['ROLE_THEM', 'ROLE_SƯA'], 'ROLE_ADMIN', true, null]">...</some-element>
 * ```
 * @example
 * @link AccountListComponent
 */

@Directive({
    selector: '[ebHasSomeAuthorityOrCondition]'
})
export class HasSomeAuthorityOrConditionDirective {
    private listAuth: any[];
    private authoritiesOrBoolean: any[];

    constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

    @Input()
    set ebHasSomeAuthorityOrCondition(auths: any[]) {
        this.listAuth = [];
        this.authoritiesOrBoolean = [];
        let isTrueAll = false;
        for (const v of auths) {
            if (typeof v === 'string') {
                this.authoritiesOrBoolean.push(v);
            } else if (typeof v === 'boolean') {
                if (v) {
                    isTrueAll = true;
                    break;
                }
            } else if (Array.isArray(v)) {
                const authorities = [];
                let isTrueAnd = true;
                for (const _v of v) {
                    if (_v === undefined || _v === 'undefined' || _v === null || _v === 'null') {
                        isTrueAnd = false;
                        break;
                    }
                    if (typeof _v === 'string') {
                        authorities.push(_v);
                    } else if (typeof _v === 'boolean') {
                        if (_v === false) {
                            isTrueAnd = false;
                            break;
                        }
                    } else {
                        isTrueAnd = !!_v;
                    }
                }
                this.listAuth.push({ authorities, isTrueAnd });
            } else {
                isTrueAll = !!v;
            }
        }
        this.updateView(isTrueAll);
        this.principal.getAuthenticationState().subscribe(identity => this.updateView(isTrueAll));
    }

    private updateView(isTrueAll: boolean): void {
        if (isTrueAll) {
            this.viewContainerRef.clear();
            this.viewContainerRef.createEmbeddedView(this.templateRef);
            return;
        }
        if (this.authoritiesOrBoolean.length > 0) {
            this.principal.hasAnyAuthority(this.authoritiesOrBoolean).then(result => {
                if (result) {
                    this.viewContainerRef.clear();
                    this.viewContainerRef.createEmbeddedView(this.templateRef);
                    return;
                }
            });
        }
        if (this.listAuth.length > 0) {
            for (let i = 0; i < this.listAuth.length; i++) {
                if (this.listAuth[i].authorities.length > 0) {
                    this.principal.hasAllAuthority(this.listAuth[i].authorities).then(result => {
                        if (result && this.listAuth[i].isTrueAnd) {
                            this.viewContainerRef.clear();
                            this.viewContainerRef.createEmbeddedView(this.templateRef);
                            return;
                        }
                    });
                }
            }
        }
    }
}
