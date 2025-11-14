% ===============================================
%  Reglas para calcular entrenamientos mínimos
% ===============================================

% --- roles requeridos ---
rol(Rol) :-
    requiere(_, Rol, _).

% --- cantidad de artistas base disponibles por rol ---
cantidad_artistas_para_rol(Rol, Cantidad) :-
    findall(A, (artista_base(A), tiene_rol(A, Rol)), L),
    sort(L, Unicos),
    length(Unicos, Cantidad).

% --- cantidad de roles faltantes por canción ---
cantidad_roles_faltantes_en_cancion(Cancion, Rol, Cantidad) :-
    requiere(Cancion, Rol, Necesarios),
    cantidad_artistas_para_rol(Rol, Disponibles),
    Faltan is Necesarios - Disponibles,
    (Faltan > 0 -> Cantidad = Faltan ; Cantidad = 0).

% --- máximo faltante por rol en todas las canciones ---
max_cantidad_roles_faltantes_en_cancion(Rol, Max) :-
    findall(D, (cancion(C), cantidad_roles_faltantes_en_cancion(C, Rol, D)), LD),
    max_list_con_cero(LD, Max).

max_list_con_cero([], 0).
max_list_con_cero(L, M) :- max_list(L, M).

max_list([X|Xs], M) :- max_list_(Xs, X, M).
max_list_([], M, M).
max_list_([X|Xs], A, M) :-
    A1 is max(A, X),
    max_list_(Xs, A1, M).

% --- total de entrenamientos mínimos para cubrir TODO el recital ---
entrenamientos_minimos(Total) :-
    findall(R, rol(R), RolesDup),
    sort(RolesDup, Roles),
    findall(M, (member(R, Roles), max_cantidad_roles_faltantes_en_cancion(R, M)), Maximos),
    sum_list(Maximos, Total).

% --- (opcional) detalle para depuración ---
detalle_roles_faltantes :-
    forall(rol(R),
           (max_cantidad_roles_faltantes_en_cancion(R, M),
            format('Rol ~w -> entrenar ~w~n', [R, M]))).
