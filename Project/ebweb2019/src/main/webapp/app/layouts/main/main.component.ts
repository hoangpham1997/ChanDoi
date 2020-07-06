import { AfterViewChecked, AfterViewInit, Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';

import { JhiLanguageHelper, Principal } from 'app/core';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { BROADCAST_EVENT } from 'app/app.constants';

@Component({
    selector: 'eb-main',
    templateUrl: './main.component.html'
})
export class EbMainComponent extends BaseComponent implements OnInit {
    eventSubscriberChangeSession: Subscription;
    isNavbarCollapsed: boolean;
    disableUserSelect: boolean;

    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        public router: Router,
        private principal: Principal,
        private eventManager: JhiEventManager
    ) {
        super();
    }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'ebwebApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnInit() {
        this.registerChangeSession();
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });

        this.principal.identity().then(account => {
            if (!account) {
                if (this.router.url.includes('/admin/login')) {
                    this.router.navigate(['/admin/login']);
                } else {
                    this.router.navigate(['/login']);
                }
            }
        });
        this.isNavbarCollapsed = true;
        this.disableUserSelect = false;
        this.eventSubscriberChangeSession = this.eventManager.subscribe(BROADCAST_EVENT.DISABLE_USER_SELECT, response => {
            this.disableUserSelect = response.content;
        });
        this.eventSubscriberChangeSession = this.eventManager.subscribe('checkEnter', res => {
            this.isEnter = res.content;
        });
    }

    registerChangeSession() {
        this.eventSubscriberChangeSession = this.eventManager.subscribe('changeSession', response => {
            this.router.navigate(['/']);
        });

        this.eventSubscriberChangeSession = this.eventManager.subscribe('toggleNavbar', response => {
            this.isNavbarCollapsed = response.content;
        });
    }

    moveEnter($event) {
        const inputs = document.getElementsByTagName('input');
        const selects = document.getElementsByTagName('select');
        const textarea = document.getElementsByTagName('textarea');
        const curIndex = $event.path['0'].tabIndex;
        let nextIndex = curIndex;
        let j = 0;
        let isFound = false;
        const element: HTMLElement = inputs[curIndex].closest('table');
        if (this.isEnter === undefined || this.isEnter === null || !this.isEnter || element === undefined || element === null) {
            while (j < 15) {
                j++;
                nextIndex = curIndex + j;
                for (let i = 0; i < inputs.length; i++) {
                    // loop over the tabs.
                    if (inputs[i].tabIndex === nextIndex && !inputs[i].disabled) {
                        // is this our tab?
                        inputs[i].focus(); // Focus and leave.
                        isFound = true;
                        break;
                    }
                }
                for (let i = 0; i < selects.length; i++) {
                    // loop over the tabs.
                    if (selects[i].tabIndex === nextIndex && !selects[i].disabled) {
                        // is this our tab?
                        selects[i].focus(); // Focus and leave.
                        isFound = true;
                        break;
                    }
                }
                for (let i = 0; i < textarea.length; i++) {
                    // loop over the tabs.
                    if (textarea[i].tabIndex === nextIndex && !textarea[i].disabled) {
                        // is this our tab?
                        textarea[i].focus(); // Focus and leave.
                        isFound = true;
                        break;
                    }
                }
                if (isFound) {
                    break;
                }
            }
        }
    }

    moveNext($event) {
        const inputs = document.getElementsByTagName('input');
        const selects = document.getElementsByTagName('select');
        const curIndex = $event.path['0'].tabIndex;
        let nextIndex = curIndex;
        let j = 0;
        let isFound = false;
        // for (let i = 0; i < inputs.length; i++) {
        //     // loop over the tabs.
        //     if (inputs[i].tabIndex === curIndex && !inputs[i].disabled) {
        //
        //     }
        // }
        const currentPosition = $event.path['0'].selectionEnd ? $event.path['0'].selectionEnd : 0;
        const lengthPosition = $event.path['0'].value.length ? $event.path['0'].value.length : 0;
        if (currentPosition === lengthPosition || $event.path['0'].type === 'checkbox') {
            while (j < 15) {
                j++;
                nextIndex = curIndex + j;
                for (let i = 0; i < inputs.length; i++) {
                    // loop over the tabs.
                    if (inputs[i].tabIndex === nextIndex && !inputs[i].disabled) {
                        // is this our tab?
                        inputs[i].focus(); // Focus and leave.
                        if ($event.path['0'].type !== 'checkbox' && inputs[i].type !== 'number') {
                            inputs[i].selectionEnd = 0;
                        }
                        isFound = true;
                        break;
                    }
                }
                for (let i = 0; i < selects.length; i++) {
                    // loop over the tabs.
                    if (selects[i].tabIndex === nextIndex && !selects[i].disabled) {
                        // is this our tab?
                        selects[i].focus(); // Focus and leave.
                        isFound = true;
                        break;
                    }
                }
                if (isFound) {
                    break;
                }
            }
        }
    }

    movePrevious($event) {
        const inputs = document.getElementsByTagName('input');
        const selects = document.getElementsByTagName('select');
        const curIndex = $event.path['0'].tabIndex;
        let nextIndex = curIndex;
        let j = 0;
        let isFound = false;
        const currentPosition = $event.path['0'].selectionEnd ? $event.path['0'].selectionEnd : 0;
        if (currentPosition === 0) {
            while (j < 15) {
                j++;
                nextIndex = curIndex - j;
                for (let i = 0; i < inputs.length; i++) {
                    // loop over the tabs.
                    if (inputs[i].tabIndex === nextIndex && !inputs[i].disabled) {
                        // is this our tab?
                        inputs[i].focus(); // Focus and leave.
                        if (inputs[i].type !== 'number' && inputs[i].type !== 'checkbox') {
                            inputs[i].selectionEnd = 0;
                        }

                        isFound = true;
                        break;
                    }
                }
                for (let i = 0; i < selects.length; i++) {
                    // loop over the tabs.
                    if (selects[i].tabIndex === nextIndex && !selects[i].disabled) {
                        // is this our tab?
                        selects[i].focus(); // Focus and leave.
                        isFound = true;
                        break;
                    }
                }
                if (isFound) {
                    break;
                }
            }
        }
    }
}
