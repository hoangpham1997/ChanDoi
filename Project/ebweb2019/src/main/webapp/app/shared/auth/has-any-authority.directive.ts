import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from 'app/core/auth/principal.service';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has any
 * of the authorities passed as the `expression` and other conditions.
 *
 * @howToUse
 * ``` conditions like *ngIf
 *
 *     <some-element *ebHasAnyAuthority="'ROLE_ADMIN'">...</some-element>
 *
 *     <some-element *ebHasAnyAuthority="['ROLE_ADMIN', 'ROLE_USER', true]">...</some-element>
 *
 *
 * ```
 */

@Directive({
    selector: '[ebHasAnyAuthority]'
})
export class HasAnyAuthorityDirective {
    private authoritiesAndBoolean: any[];

    constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

    @Input()
    set ebHasAnyAuthority(value: any[]) {
        this.authoritiesAndBoolean = [];
        let isTrue = true;
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

        this.updateView(isTrue);
        // Get notified each time authentication state changes.
        this.principal.getAuthenticationState().subscribe(identity => this.updateView(isTrue));
    }

    private updateView(isTrue: boolean): void {
        this.principal.hasAnyAuthority(this.authoritiesAndBoolean).then(result => {
            this.viewContainerRef.clear();
            if (result && isTrue) {
                this.viewContainerRef.createEmbeddedView(this.templateRef);
            }
        });
    }
}
