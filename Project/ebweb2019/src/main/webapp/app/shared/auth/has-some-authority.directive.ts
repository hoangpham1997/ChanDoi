import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from 'app/core/auth/principal.service';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has some
 * of the authorities passed as the `expression` or other conditions.
 *
 * @howToUse
 * ``` conditions like *ngIf
 *     <some-element *HasSomeAuthorityDirective="'ROLE_ADMIN'">...</some-element>
 *
 *     <some-element *HasSomeAuthorityDirective="['ROLE_ADMIN', 'ROLE_USER', true, null, ]">...</some-element>
 * ```
 */

@Directive({
    selector: '[ebHasSomeAuthority]'
})
export class HasSomeAuthorityDirective {
    private authoritiesOrBoolean: any[];

    constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {}

    @Input()
    set ebHasSomeAuthority(value: any[]) {
        this.authoritiesOrBoolean = [];
        let isTrue = false;
        for (const v of value) {
            if (typeof v === 'string') {
                this.authoritiesOrBoolean.push(v);
            } else if (typeof v === 'boolean') {
                if (v === true) {
                    isTrue = true;
                }
            } else {
                isTrue = !!v;
                this.authoritiesOrBoolean = [...this.authoritiesOrBoolean];
            }
        }

        this.updateView(isTrue);
        // Get notified each time authentication state changes.
        this.principal.getAuthenticationState().subscribe(identity => this.updateView(isTrue));
    }

    private updateView(isTrue: boolean): void {
        this.principal.hasAnyAuthority(this.authoritiesOrBoolean).then(result => {
            this.viewContainerRef.clear();
            if (result || isTrue) {
                this.viewContainerRef.createEmbeddedView(this.templateRef);
            }
        });
    }
}
