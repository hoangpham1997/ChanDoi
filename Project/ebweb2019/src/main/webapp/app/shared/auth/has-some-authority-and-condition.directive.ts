import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from 'app/core/auth/principal.service';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has some
 * of the authorities passed as the `expression` or other conditions.
 *
 * @howToUse
 * ``` conditions like *ngIf
 *      Tham số đầu tiên là mảng các role và Condition mà chỉ cần có 1 trong các điều kiện đúng sẽ trả về true
 *      Hiển thị element khi tất cả các tham số đề true( các tham số 2+ có thể là role, object, condition,...)
 *     <some-element *ebHasSomeAuthorityAndCondition="[['ROLE_ADMIN', 'ROLE_USER'], true, null, ]">...</some-element>
 * ```
 */

@Directive({
    selector: '[ebHasSomeAuthorityAndCondition]'
})
export class HasSomeAuthorityAndConditionDirective {
    private authoritiesOrBoolean: any[];
    private authoritiesAndBoolean: any[];
    private checkCondition: boolean;
    constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

    @Input()
    set ebHasSomeAuthorityAndCondition(_value: any[]) {
        const value = _value[0];
        this.authoritiesOrBoolean = [];
        let isTrue = false;
        let isHadCheckBoolean = false;
        for (const v of value) {
            if (typeof v === 'string') {
                this.authoritiesOrBoolean.push(v);
            } else if (typeof v === 'boolean') {
                if (v === true) {
                    isTrue = true;
                    isHadCheckBoolean = true;
                }
            } else {
                isTrue = !!v;
                isHadCheckBoolean = true;
                this.authoritiesOrBoolean = [...this.authoritiesOrBoolean];
            }
        }
        if (_value.length > 1) {
            isTrue = this.checkAnyParam(_value.slice(1), isHadCheckBoolean ? isTrue : true);
        }
        this.updateView(isTrue);
        // Get notified each time authentication state changes.
        this.principal.getAuthenticationState().subscribe(identity => this.updateView(isTrue));
    }

    checkAnyParam(value: any[], isTrue: boolean) {
        this.authoritiesAndBoolean = [];
        this.checkCondition = true;
        for (const v of value) {
            if (v === undefined || v === 'undefined' || v === null || v === 'null') {
                isTrue = false;
                break;
            }
            if (typeof v === 'string') {
                this.authoritiesAndBoolean.push(v);
            } else if (typeof v === 'boolean') {
                if (v === false) {
                    isTrue = false;
                    break;
                }
            } else {
                isTrue = !!v;
                this.authoritiesAndBoolean = [...this.authoritiesAndBoolean];
            }
        }
        return isTrue;
    }

    private updateView(isTrue: boolean): void {
        if (this.checkCondition) {
            if (this.authoritiesAndBoolean && this.authoritiesAndBoolean.length > 0) {
                this.principal.hasAllAuthority(this.authoritiesAndBoolean).then(result => {
                    this.viewContainerRef.clear();
                    if (result && isTrue) {
                        this.principal.hasAnyAuthority(this.authoritiesOrBoolean).then(resultAny => {
                            if (resultAny) {
                                this.viewContainerRef.createEmbeddedView(this.templateRef);
                            }
                        });
                    }
                });
            } else {
                this.principal.hasAnyAuthority(this.authoritiesOrBoolean).then(result => {
                    this.viewContainerRef.clear();
                    if (result && isTrue) {
                        this.viewContainerRef.createEmbeddedView(this.templateRef);
                    }
                });
            }
        } else {
            this.principal.hasAnyAuthority(this.authoritiesOrBoolean).then(result => {
                this.viewContainerRef.clear();
                if (result || isTrue) {
                    this.viewContainerRef.createEmbeddedView(this.templateRef);
                }
            });
        }
    }
}
