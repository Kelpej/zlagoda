import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {HttpClient} from "@angular/common/http";
import {AppState, getUser, isManager, setUser} from "./state";
import {Store} from "@ngrx/store";
import {Employee} from "./services/employee/employee.service";
import {first, Observable} from "rxjs";

enum Role {
  MANAGER,
  CASHIER
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {
  isLoading = true;

  constructor(public dialog: MatDialog,
              private httpClient: HttpClient,
              private store: Store<AppState>) {}

  ngOnInit(): void {
    this.httpClient.get<Employee>("/employees/me").subscribe(user => {
      this.store.dispatch(setUser({ user }));
      this.isLoading = false;
    })
  }

  public isManager(): Observable<boolean> {
    return this.store.select(isManager);
  }

  public user(): Observable<Employee> {
    return this.store.select(getUser);
  }
  title = 'Zlagoda';
}
