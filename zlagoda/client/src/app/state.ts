import {Employee} from "./services/employee/employee.service";
import {createAction, createReducer, createSelector, on, props} from "@ngrx/store";

export interface AppState {
  user: Employee | null
}

export const initialState: AppState = {
  user: null,
}

export const setUser = createAction('[User Component] Set User', props<{ user: Employee }>());

export const appReducer = createReducer(
  initialState,
  on(setUser, (state, { user }) => ({ ...state, user }))
)

export const getUser = (state: any) => state.app.user;
export const isManager = (state: any) => state.app.user.role === 'MANAGER';
