% ---------- Roles del recital ----------
rol(Rol) :-
    requiere(_, Rol, _).

% ---------- Disponibilidad base ----------
cantidad_artistas_base_para_rol(Rol, Cantidad) :-
    findall(A, (artista_base(A), tiene_rol(A, Rol)), L),
    sort(L, Unicos),
    length(Unicos, Cantidad).

% ---------- Déficit por canción ----------
cantidad_roles_faltantes_en_cancion(Cancion, Rol, Cantidad) :-
    requiere(Cancion, Rol, Necesarios),
    cantidad_artistas_base_para_rol(Rol, Disponibles),
    Faltan is Necesarios - Disponibles,
    ( Faltan > 0 -> Cantidad = Faltan ; Cantidad = 0 ).

% ---------- Máximo déficit por rol en todo el recital ----------
max_cantidad_roles_faltantes_en_cancion(Rol, Max) :-
    findall(D, (cancion(C), cantidad_roles_faltantes_en_cancion(C, Rol, D)), LD),
    ( LD == [] -> Max = 0 ; max_list(LD, Max) ).

% ---------- Entrenamientos mínimos teóricos (suma de máximos) ----------
entrenamientos_minimos_teoricos(Total) :-
    findall(R, rol(R), RolesDup),
    sort(RolesDup, Roles),
    findall(M, (member(R, Roles), max_cantidad_roles_faltantes_en_cancion(R, M)), Maximos),
    sum_list(Maximos, Total).

% ---------- Stock de entrenables = cantidad de externos recibidos ----------
entrenable(A) :-
    artista_externo(A),
    \+ banda(A, _).

stock_entrenables(N) :-
    findall(A, entrenable(A), L),
    sort(L, Unicos),
    length(Unicos, N).
    
% ---------- Entrenamientos mínimos factibles (respeta stock) ----------
%   predicado principal a consultar: entrenamientos_minimos/1
entrenamientos_minimos(Tfact) :-
    entrenamientos_minimos_teoricos(T),
    stock_entrenables(N),
    ( N >= T -> Tfact = T ; Tfact = N ).

% ---------- Costo total (opcional, uniforme) ----------
costo_total_uniforme(CT) :-
    entrenamientos_minimos(Tfact),
    ( costo_uniforme(C) -> CT is Tfact * C ; CT is 0 ).

% ---------- Detalles (opcionales) ----------
detalle_roles_faltantes :-
    forall(rol(R),
           ( max_cantidad_roles_faltantes_en_cancion(R, M),
             format('rol ~w -> entrenar ~w~n', [R, M]) )).

detalle_entrenables :-
    findall(A, artista_externo(A), L),
    sort(L, Unicos),
    length(Unicos, N),
    format('externos/entrenables (~w): ~w~n', [N, Unicos]).