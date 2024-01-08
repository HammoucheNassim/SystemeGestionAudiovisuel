open Printf

type materiel = {
  nom : string;
  mutable stock : int;
}

let initialiser_stock () =
  ["Lumière LED", 30; "Lumière Stroboscopique", 30; "Lumière Ambiante", 30;
   "Enceinte Bluetooth", 30; "Enceinte Professionnelle", 30; "Estrade Simple", 30;
   "Estrade Double", 30; "Estrade VIP", 30; "Machine à fumée Standard", 30;
   "Machine à fumée Professionnelle", 30; "Machine à fumée Puissante", 30]
  |> List.map (fun (n, s) -> {nom = n; stock = s})

let split_on_char c s =
  let rec aux c s n =
    try
      let p = String.index_from s n c in
      String.sub s n (p - n) :: aux c s (p + 1)
    with Not_found -> [String.sub s n (String.length s - n)]
  in
  aux c s 0

let ajouter_enregistrement fichier nom_materiel quantite =
  let oc = open_out_gen [Open_append; Open_creat] 0o666 fichier in
  Printf.fprintf oc "%s,%d\n" nom_materiel quantite;
  close_out oc

let lire_fichier fichier =
  let ic = open_in fichier in
  try
    while true do
      let line = input_line ic in
      Printf.printf "%s\n" line
    done
  with End_of_file -> close_in ic

let mettre_a_jour_fichier fichier nom_materiel nouvelle_quantite =
  let lines = ref [] in
  let ic = open_in fichier in
  try
    while true do
      let line = input_line ic in
      match split_on_char ',' line with
      | nom :: _ when nom = nom_materiel ->
          lines := (nom_materiel ^ "," ^ string_of_int nouvelle_quantite) :: !lines
      | _ -> lines := line :: !lines
    done
  with End_of_file ->
    close_in ic;
    let oc = open_out fichier in
    List.iter (fun line -> Printf.fprintf oc "%s\n" line) (List.rev !lines);
    close_out oc

let supprimer_enregistrement fichier nom_materiel =
  let lines = ref [] in
  let ic = open_in fichier in
  try
    while true do
      let line = input_line ic in
      match split_on_char ',' line with
      | nom :: _ when nom <> nom_materiel -> lines := line :: !lines
      | _ -> ()
    done
  with End_of_file ->
    close_in ic;
    let oc = open_out fichier in
    List.iter (fun line -> Printf.fprintf oc "%s\n" line) (List.rev !lines);
    close_out oc

let () =
  let fichier_csv = "chemin_vers_votre_fichier.csv" in
  ajouter_enregistrement fichier_csv "Nouveau Materiel" 10;
  lire_fichier fichier_csv;
  mettre_a_jour_fichier fichier_csv "Nouveau Materiel" 15;
  supprimer_enregistrement fichier_csv "Nouveau Materiel"
